import pandas as pd
import matplotlib.pyplot as plt

df = pd.read_csv("../insurance.csv")

# Histogramme de l'âge
plt.hist(df["age"], bins=10)
plt.title("Distribution de l'âge")
plt.xlabel("Âge")
plt.ylabel("Fréquence")
plt.show()

# Histogramme du BMI
plt.hist(df["bmi"], bins=10)
plt.title("Distribution du BMI")
plt.xlabel("BMI")
plt.ylabel("Fréquence")
plt.show()