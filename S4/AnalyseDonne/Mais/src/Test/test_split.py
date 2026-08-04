import numpy as np
import sys
import os
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), "..")))

import numpy as np
from split import trouver_meilleur_split


# Variable explicative (pct_rouille)
X = np.array([5, 10, 20, 35])

# Labels
# 0 = saine
# 1 = malade
y = np.array([0, 0, 1, 1])

# Recherche du meilleur split
seuil, purete = trouver_meilleur_split(X, y)

print("=== Résultat ===")
print("Meilleur seuil :", seuil)
print("Meilleure pureté :", purete)