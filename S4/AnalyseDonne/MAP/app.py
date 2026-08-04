from flask import Flask, render_template, request, jsonify
import pandas as pd
import joblib
import sqlite3
from datetime import datetime

app = Flask(__name__)

# =========================
# CHARGEMENT MODELE IA
# =========================
model = joblib.load("terrain_model.pkl")
encoders = joblib.load("encoders.pkl")


# =========================
# INIT DATABASE
# =========================
def init_db():

    conn = sqlite3.connect("terrain.db")
    c = conn.cursor()

    c.execute("""
        CREATE TABLE IF NOT EXISTS predictions (

            id INTEGER PRIMARY KEY AUTOINCREMENT,

            latitude REAL,
            longitude REAL,

            acces TEXT,
            distance REAL,
            batisable TEXT,
            distance_jirama REAL,
            papier TEXT,
            commune TEXT,

            prix_predi REAL,

            date TEXT
        )
    """)

    conn.commit()
    conn.close()


init_db()


# =========================
# SAVE PREDICTION
# =========================
def save_prediction(data, prix):

    conn = sqlite3.connect("terrain.db")
    c = conn.cursor()

    c.execute("""
        INSERT INTO predictions (

            latitude,
            longitude,

            acces,
            distance,
            batisable,
            distance_jirama,
            papier,
            commune,

            prix_predi,
            date

        )

        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)

    """, (

        data["latitude"],
        data["longitude"],

        data["acces"],
        data["distance"],
        data["batisable"],
        data["distance_jirama"],
        data["papier"],
        data["commune"],

        prix,

        datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    ))

    conn.commit()
    conn.close()


# =========================
# GET HISTORY
# =========================
def get_history():

    conn = sqlite3.connect("terrain.db")
    c = conn.cursor()

    c.execute("""
        SELECT *
        FROM predictions
        ORDER BY id DESC
    """)

    rows = c.fetchall()

    conn.close()

    return rows


# =========================
# PAGE PRINCIPALE
# =========================
@app.route("/")
def home():

    history = get_history()

    return render_template(
        "index.html",
        history=history
    )


# =========================
# API PREDICTION
# =========================
@app.route("/predict", methods=["POST"])
def predict():

    try:

        # =========================
        # RECUPERATION JSON
        # =========================
        data = request.get_json()

        # =========================
        # DONNEES ORIGINALES
        # =========================
        original_data = {

            "latitude":
                float(data["latitude"]),

            "longitude":
                float(data["longitude"]),

            "acces":
                data["acces"],

            "distance":
                float(data["distance"]),

            "batisable":
                data["batisable"],

            "distance_jirama":
                float(data["distance_jirama"]),

            "papier":
                data["papier"],

            "commune":
                data["commune"]
        }

        # =========================
        # VALIDATION SIMPLE
        # =========================
        if original_data["distance"] < 0:
            return jsonify({
                "success": False,
                "error": "Distance invalide"
            })

        if original_data["distance_jirama"] < 0:
            return jsonify({
                "success": False,
                "error": "Distance JIRAMA invalide"
            })

        # =========================
        # DONNEES POUR ML
        # =========================
        ml_data = {

            "acces":
                original_data["acces"],

            "distance":
                original_data["distance"],

            "batisable":
                original_data["batisable"],

            "distance_jirama":
                original_data["distance_jirama"],

            "papier":
                original_data["papier"],

            "commune":
                original_data["commune"]
        }

        # =========================
        # ENCODAGE
        # =========================
        for col in encoders:

            ml_data[col] = encoders[col] \
                .transform([ml_data[col]])[0]

        # =========================
        # DATAFRAME
        # =========================
        df = pd.DataFrame([ml_data])

        # =========================
        # PREDICTION IA
        # =========================
        prix = model.predict(df)[0]

        # =========================
        # ARRONDI
        # =========================
        prix = round(prix)

        # =========================
        # SECURITE ANTI NEGATIF
        # =========================
        prix = max(5000, prix)

        # =========================
        # SAVE DATABASE
        # =========================
        save_prediction(original_data, prix)

        # =========================
        # RESPONSE JSON
        # =========================
        return jsonify({

            "success": True,

            "prix":
                prix

        })

    except Exception as e:

        return jsonify({

            "success": False,

            "error":
                str(e)

        })


# =========================
# API HISTORIQUE
# =========================
@app.route("/history")
def history():

    rows = get_history()

    result = []

    for row in rows:

        result.append({

            "id": row[0],

            "latitude": row[1],
            "longitude": row[2],

            "acces": row[3],
            "distance": row[4],
            "batisable": row[5],
            "distance_jirama": row[6],
            "papier": row[7],
            "commune": row[8],

            "prix_predi": row[9],

            "date": row[10]
        })

    return jsonify(result)


# =========================
# RUN APP
# =========================
if __name__ == "__main__":

    app.run(
        debug=True
    )