#pragma once

#include <SFML/Graphics.hpp>
#include "vitesse.hpp"
#include "Compteur.hpp"
#include "InfoBar.hpp"
#include "Freinage.hpp"

class Cockpit
{
private:
    // --- Arrière-plan ---
    sf::RectangleShape windshield;
    sf::RectangleShape cloud;
    sf::RectangleShape dashboard;

    // --- Instruments de vitesse ---
    vitesse Vx;
    vitesse Vy;
    vitesse Vtot;

    // --- Compteur Altitude + Distance piste ---
    Compteur compteur;

    // --- Barre info : V décrochage, yx, yy ---
    InfoBar infoBar;

    // --- Freinage X et Y ---
    Freinage freinageX;
    Freinage freinageY;

    // --- Boutons de direction ---
    sf::RectangleShape btnAvant;
    sf::RectangleShape btnArriere;
    sf::RectangleShape btnGauche;
    sf::RectangleShape btnDroite;
    sf::RectangleShape btnBas;

    sf::Font font;
    sf::Text txtAvant;
    sf::Text txtArriere;
    sf::Text txtGauche;
    sf::Text txtDroite;
    sf::Text txtBas;

    // --- Chronomètre ---
    sf::RectangleShape chronoBackground;
    sf::Text chronoText;
    sf::Clock chronoClock;
    sf::Time chronoElapsed;   // temps accumulé avant pause
    bool chronoPaused;

    // --- Bouton Pause ---
    sf::RectangleShape btnPause;
    sf::CircleShape    pauseIconCircle;  // cercle décoratif
    sf::Text           txtPause;

    // Helpers
    void setupButton(sf::RectangleShape& btn, sf::Text& txt,
                     const std::string& label,
                     float x, float y, float w = 90.f, float h = 35.f);

    std::string formatTime(sf::Time t) const;

public:
    Cockpit();

    void handleEvent(const sf::Event& event, sf::RenderWindow& window);
    void update();
    void draw(sf::RenderWindow& window);
};