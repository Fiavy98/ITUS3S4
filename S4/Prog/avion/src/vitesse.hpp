#pragma once
#include <SFML/Graphics.hpp>

class vitesse
{
private:
    sf::Texture         texture;
    sf::RenderTexture   renderTexture;
    sf::CircleShape     circle;
    sf::RectangleShape aiguille;
    sf::Font font;
    sf::Text text;
    sf::Text            valueText;

    float               taille;
    float currentSpeed;
    float maxSpeed;

    sf::Vector2f centre; //Centre du compteur a l'ecran


public:
    vitesse(float x, float y, float taille = 180.f,float maxSpeed=350.f); 
    void setPosition(float x, float y);
    void setSpeed(float speed);
    void setFont(const sf::Font& f);
    void draw(sf::RenderWindow& window);
};