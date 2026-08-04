#!/usr/bin/env python3
"""
Database setup script - Run this before starting the application
"""
import sys
from config.db import initialize_database

print("Setting up Solar Panel database...")
if initialize_database():
    print("✓ Database initialized successfully!")
    print("You can now run: python3 main.py")
else:
    print("✗ Failed to initialize database.")
    print("Make sure MySQL is running:")
    print("  sudo service mysql start")
    sys.exit(1)