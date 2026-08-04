from flask import Flask, render_template, request
import joblib
import pandas as pd
import sqlite3
from datetime import datetime

app = Flask(__name__)

# =========================
# CHARGEMENT MODELE
# =========================
model = joblib.load("terrain_model.pkl")
encoders = joblib.load("encoders.pkl")


# =========================
# INIT DATABASE (IMPORTANT)
# =========================
def init_db():
    conn = sqlite3.connect("terrain.db")
    c = conn.cursor()

    c.execute("""
    CREATE TABLE IF NOT EXISTS predictions (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
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


init_db()  # ⚠️ obligatoire au démarrage


# =========================
# SAVE PREDICTION
# =========================
def save_prediction(data, prix):
    conn = sqlite3.connect("terrain.db")
    c = conn.cursor()

    c.execute("""
        INSERT INTO predictions (
            acces, distance, batisable,
            distance_jirama, papier, commune,
            prix_predi, date
        )
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
    """, (
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

    c.execute("SELECT * FROM predictions ORDER BY id DESC")
    rows = c.fetchall()

    conn.close()
    return rows


# =========================
# HOME PAGE
# =========================
@app.route("/")
def home():
    history = get_history()
    return render_template("index.html", history=history)


# =========================
# PREDICTION ROUTE
# =========================
@app.route("/predict", methods=["POST"])
def predict():

    data = {
        "acces": request.form["acces"],
        "distance": float(request.form["distance"]),
        "batisable": request.form["batisable"],
        "distance_jirama": float(request.form["distance_jirama"]),
        "papier": request.form["papier"],
        "commune": request.form["commune"]
    }

    # =========================
    # ENCODAGE LABELENCODER
    # =========================
    for col in encoders:
        if col in data:
            data[col] = encoders[col].transform([data[col]])[0]

    # =========================
    # PREDICTION
    # =========================
    df = pd.DataFrame([data])
    prix = model.predict(df)[0]

    # =========================
    # SAVE INTO DB
    # =========================
    save_prediction(request.form, round(prix))

    # =========================
    # LOAD HISTORY
    # =========================
    history = get_history()

    return render_template(
        "index.html",
        prediction=round(prix),
        history=history
    )


# =========================
# RUN APP
# =========================
if __name__ == "__main__":
    app.run(debug=True)