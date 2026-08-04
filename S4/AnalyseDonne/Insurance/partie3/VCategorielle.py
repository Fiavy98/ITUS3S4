import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt

df = pd.read_csv("../insurance.csv")

plt.figure(figsize=(8,5))
sns.countplot(x="region", hue="smoker", data=df)

plt.title("Fumeurs et non-fumeurs par région")
plt.show()