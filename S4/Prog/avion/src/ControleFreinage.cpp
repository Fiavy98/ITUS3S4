#include "ControleFreinage.hpp"

ControleFreinage::ControleFreinage(const std::string& nomAxe, float x, float y)
{
    valeurFreinage = 0.0f;

    // Chargement de la police
    police.loadFromFile("assets/fonts/arial.ttf");

    // Cadre de fond gris métallique pour le bloc de freinage
    fondCadre.setSize(sf::Vector2f(150, 120));
    fondCadre.setPosition(x, y);
    fondCadre.setFillColor(sf::Color(80, 80, 80));
    fondCadre.setOutlineThickness(1);
    fondCadre.setOutlineColor(sf::Color(120, 120, 120));

    // Texte du titre (ex: "Freinage x")
    titre.setFont(police);
    titre.setString(nomAxe);
    titre.setCharacterSize(14);
    titre.setFillColor(sf::Color::White);
    titre.setPosition(x + 10, y + 8);

    // Bouton Plus [+]
    btnPlus.setSize(sf::Vector2f(45, 35));
    btnPlus.setPosition(x + 15, y + 40);
    btnPlus.setFillColor(sf::Color(110, 110, 110));
    btnPlus.setOutlineThickness(1);
    btnPlus.setOutlineColor(sf::Color::Black);

    textePlus.setFont(police);
    textePlus.setString("+");
    textePlus.setCharacterSize(20);
    textePlus.setFillColor(sf::Color::White);
    textePlus.setPosition(x + 31, y + 45);

    // Bouton Moins [-]
    btnMoins.setSize(sf::Vector2f(45, 35));
    btnMoins.setPosition(x + 80, y + 40);
    btnMoins.setFillColor(sf::Color(110, 110, 110));
    btnMoins.setOutlineThickness(1);
    btnMoins.setOutlineColor(sf::Color::Black);

    texteMoins.setFont(police);
    texteMoins.setString("-");
    texteMoins.setCharacterSize(20);
    texteMoins.setFillColor(sf::Color::White);
    texteMoins.setPosition(x + 98, y + 43);
}

void ControleFreinage::draw(sf::RenderWindow& window)
{
    // Dessin du bloc complet
    window.draw(fondCadre);
    window.draw(titre);
    
    window.draw(btnPlus);
    window.draw(textePlus);
    
    window.draw(btnMoins);
    window.draw(texteMoins);
}