#pragma once
#include <SFML/Graphics.hpp>
#include "Compteur.hpp"
#include "vitesse.hpp"
#include "ControleFreinage.hpp"
#include "PareBrise.hpp"

class Cockpit
{
private:
    sf::Font police;

    // Pare-brise (remplace windshield + cloud)
    PareBrise pareBrise;

    // DashBoard
    sf::RectangleShape dashboard;

    // Vitesse
    vitesse Vx;
    vitesse Vy;
    vitesse Vtot;

    // Compteurs
    Compteur altiude;
    Compteur distPiste;
    Compteur Vcrochage;
    Compteur limiteX;
    Compteur limiteY;

    // Freinage
    ControleFreinage freinageX;
    ControleFreinage freinageY;

    // Boutons direction vue
    sf::RectangleShape btnAvant;
    sf::RectangleShape btnArriere;
    sf::RectangleShape btnGauche;
    sf::RectangleShape btnDroite;
    sf::RectangleShape btnArriereCentre;
    sf::Text txtAvant;
    sf::Text txtArriere;
    sf::Text txtGauche;
    sf::Text txtDroite;
    sf::Text txtArriereCentre;

    // Chronometre
    sf::Clock horloge;
    sf::Time  tempsEcoule;
    bool      estEnPause;
    sf::Text  txtChrono;
    sf::RectangleShape btnPause;
    sf::Text  txtPause;

public:
    Cockpit();
    void update();
    void updateVitese(float vx_ms, float vy_ms, float vtot_ms);
    void setVy(float vy);  // ← pour faire bouger les nuages
    void draw(sf::RenderWindow& window);
    void gererClic(sf::Vector2i positionSouris, sf::RenderWindow& window);
    bool getPause() const { return estEnPause; }
};