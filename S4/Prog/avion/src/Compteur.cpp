#include "Compteur.hpp"
#include <string>
#include <iostream> // Pour debugger au cas où la police ne charge pas

Compteur::Compteur(const std::string& nom, float x, float y)
{
    label = nom;
    valeur = 0;

    // Assurez-vous que le chemin vers votre police est correct
    if (!police.loadFromFile("assets/fonts/arial.ttf")) {
        // Optionnel : gérer l'erreur si la police ne charge pas
    }
    
    // On lie explicitement la police au texte (TRÈS IMPORTANT en SFML)
    texte.setFont(police);
    texte.setCharacterSize(16); // Taille lisible pour le tableau de bord
    texte.setFillColor(sf::Color::White);

    // Ajustement de la boîte pour ressembler à une ligne textuelle
    body.setSize(sf::Vector2f(220, 70)); 
    body.setPosition(x, y);
    body.setFillColor(sf::Color(30, 30, 30)); // Un gris très foncé discret
    body.setOutlineThickness(1);
    body.setOutlineColor(sf::Color(100, 100, 100));

    texte.setString(label + " : " + std::to_string((int)valeur));
    // Centrage vertical approximatif du texte dans la boîte de hauteur 30
    texte.setPosition(x + 10, y + 4); 
}

void Compteur::setValeur(float v)
{
    valeur = v;
    texte.setString(label + " : " + std::to_string((int)valeur));
}

void Compteur::draw(sf::RenderWindow& window)
{
    window.draw(body);
    window.draw(texte);
}