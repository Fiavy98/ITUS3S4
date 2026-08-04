import pandas as pd
import joblib

from sklearn.preprocessing import LabelEncoder
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestRegressor
from sklearn.metrics import mean_absolute_error

# 1. lecture
df = pd.read_csv("terrain.csv")

# 2. nettoyage simple
df = df.dropna()

# 3. encoders
encoders = {}

cols = ["acces", "batisable", "papier", "commune"]

for col in cols:
    enc = LabelEncoder()
    df[col] = enc.fit_transform(df[col])
    encoders[col] = enc

# 4. X / y
X = df.drop("prix_m2", axis=1)
y = df["prix_m2"]

# 5. train/test
X_train, X_test, y_train, y_test = train_test_split(
    X, y, test_size=0.2, random_state=42
)

# 6. model
model = RandomForestRegressor()
model.fit(X_train, y_train)

# 7. evaluation
pred = model.predict(X_test)
error = mean_absolute_error(y_test, pred)

print("Erreur:", error)

# 8. SAVE MODEL + ENCODERS
joblib.dump(model, "terrain_model.pkl")
joblib.dump(encoders, "encoders.pkl")

print("Model + encoders saved")