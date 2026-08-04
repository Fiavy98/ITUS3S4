#include <SFML/Graphics.hpp>
#include "Cockpit.hpp"

int main()
{
    sf::RenderWindow window(sf::VideoMode(1600, 900), "Simulation Avion");

    Cockpit cockpit;

    // Valeurs de test (à remplacer par ta physique)
    float vx = 200.f, vy = 0.f;

    while(window.isOpen())
    {
        sf::Event event;
        while(window.pollEvent(event))
        {
            if(event.type == sf::Event::Closed)
                window.close();

            if(event.type == sf::Event::MouseButtonPressed)
                if(event.mouseButton.button == sf::Mouse::Left)
                    cockpit.gererClic(
                        sf::Vector2i(event.mouseButton.x, event.mouseButton.y),
                        window
                    );
        }

        // Transmettre Vy aux nuages pour faire monter/descendre le ciel
        cockpit.setVy(vy);
        cockpit.updateVitese(vx / 3.6f, vy / 3.6f,
                             std::sqrt(vx*vx + vy*vy) / 3.6f);
        cockpit.update();

        window.clear();
        cockpit.draw(window);
        window.display();
    }
    return 0;
}