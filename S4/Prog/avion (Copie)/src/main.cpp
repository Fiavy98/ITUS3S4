#include <SFML/Graphics.hpp>
#include "Cockpit.hpp"

int main()
{
    sf::RenderWindow window(
        sf::VideoMode(1600, 900),
        "Simulation Avion"
    );

    Cockpit cockpit;

    while (window.isOpen())
    {
        sf::Event event;

        while (window.pollEvent(event))
        {
            if (event.type == sf::Event::Closed)
                window.close();

            // Transmettre les évènements au cockpit
            // (bouton pause, clics freinage, etc.)
            cockpit.handleEvent(event, window);
        }

        cockpit.update();

        window.clear();
        cockpit.draw(window);
        window.display();
    }

    return 0;
}