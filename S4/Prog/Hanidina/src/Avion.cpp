#include "Avion.hpp"
#include <iostream>

Avion::Avion(float largeur, float hauteur)
    :largeur(largeur),hauteur(hauteur)
{

}

bool Avion::load(const std::string& filename){
    sprite.setTexture(texture);
    sprite.setScale(0.8f, 0.8f);
    
    // Taille du sprite après scale
    sf::Vector2u texSize = texture.getSize();
    
    float width  = texSize.x * 0.8f;
    float height = texSize.y * 0.8f;
    
    // Centre de la fenêtre (1600x350 ici)
    float centerX = 1600.f / 2.f;
    float centerY = 350.f / 2.f;
    
    // Positionner le sprite au centre (en ajustant l'origine)
    sprite.setOrigin(texSize.x / 2.f, texSize.y / 2.f);
    sprite.setPosition(centerX, centerY);

    if (!texture.loadFromFile(filename))
    {
        std::cerr << "Impossible de charger : "
                  << filename << std::endl;
        return false;
    }

    sprite.setTexture(texture);
    sprite.setScale(0.8f, 0.8f);

    return true;
}

void Avion::draw(sf::RenderWindow& window)
{
    window.draw(sprite);
}