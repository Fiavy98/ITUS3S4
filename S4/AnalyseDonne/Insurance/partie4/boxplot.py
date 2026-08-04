import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt

df = pd.read_csv("../insurance.csv")

# Age
plt.figure()
sns.boxplot(y=df["age"])
plt.title("Boxplot de l'âge")
plt.show()

# BMI
plt.figure()
sns.boxplot(y=df["bmi"])
plt.title("Boxplot du BMI")
plt.show()

# Charges
plt.figure()
sns.boxplot(y=df["charges"])
plt.title("Boxplot des charges")
plt.show()