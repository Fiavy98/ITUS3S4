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

