import os

choice = input("1 = feature + training | 2 = training only : ")

if choice == "1":
    print("Feature Engineering...")
    os.system("python3 1_feature_engineering.py")

print("Training Model...")
os.system("python3 2_model_training.py")

print("Done ✔")