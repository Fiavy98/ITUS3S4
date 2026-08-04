## Cree un fonction 
- sf::TypeRetour nomFonction(Parametre/fonction/code)
``c++
    sf::RectangleShape dashboard(
        sf::Vector2f(900,250)
    );

## Appellation utilisation de fonction
    dashboard.setPosition(350,550);
    dashboard.setFillColor(
        sf::Color(80,80,80)
    );

## Classe 
- Classe.hpp (declaration)
    - Carte d'identite
    - il dit "quest ce que le classe contient et qu est ce qu il peut faire"
```cpp
class Gauge {
private:
    float value;

public:
    void setValue(float v);
    float getValue();
    void draw();
};
```

- Classe.cpp
    - le moteur : code reeller
    - il dit : "Comment le fonction marche reeleement "
```cpp
#include "Gauge.hpp"

void Gauge::setValue(float v) {
    value = v;
}

float Gauge::getValue() {
    return value;
}

void Gauge::draw() {
    // code SFML pour afficher la jauge
}
```
## Utiliser le le methode declarer dans hpp dans cpp
Gauge::Gauge

## Gestion de clic
- dans public du hpp
```cpp
void gererClic(sf::Vector2i positionSouris, sf::RenderWindow& window);
```
- dans cpp
```cpp
    void Cockpit::gererClic(sf::Vector2i positionSouris,sf::RenderWindow& window){
        // Conventir le pixel du souris en cordoones reelle du jeux
        sf::Vector2f coordSouris = window.mapPixelToCoords(positionSouris);

        // Verifier si la souris est a l'interieure du bouton en pausse 
        if(btnPause=getGlobalBounds().contains(coordSouris)){
            // Inverser l'Etat 
            estEnPause = ! estEnPause;

            // Changer l'etat
            if (estEnPause){
                txtPause.setString("|>");
                btnPause.setFillColor(sf::Color(100, 50, 50));
            }else {
                txtPause.setString("II"); // Symbole "Pause" quand le jeu tourne
                btnPause.setFillColor(sf::Color(70, 70, 70));   // Reprend sa couleur grise
            }

            sf::FloatRect pBounds = txtPause.getLocalBounds();
                txtPause.setPosition(
                btnPause.getPosition().x + (btnPause.getSize().x - pBounds.width) / 2.f - pBounds.left,
                btnPause.getPosition().y + (btnPause.getSize().y - pBounds.height) / 2.f - pBounds.top
            );  
        }

    }
```

- Appelerle fonction dans main 
    - dans boucle while
```cpp
if (event.type == sf::Event::MouseButtonPressed)
    {
        // Si c'est un clic gauche
        if (event.mouseButton.button == sf::Mouse::Left)
        {
            // Récupérer la position de la souris
            sf::Vector2i positionSouris(event.mouseButton.x, event.mouseButton.y);
            
            // On transmet le clic au cockpit pour qu'il vérifie le bouton Pause
            monCockpit.gererClic(positionSouris, window); 
        }
    }
```

## Ajouter un vrai image
- dans hpp
```cpp
private:
    sf::Texture texture;
    sf::Sprite sprite;
```
- dans cpp
```cpp
    texture.loadFromFile("/assets/img/sary");
    sprite.setTexture(texture);
    sprite.setPosition(x,y);
```

## Position 
```cpp 
    sf::Vector2f fenetre // peut contient un position x et y

```