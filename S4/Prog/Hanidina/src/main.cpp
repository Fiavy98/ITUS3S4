#include <SFML/Graphics.hpp>
#include "Cockpit.hpp"

int main(){
    sf::RenderWindow window(sf::VideoMode(1600, 900), "Ndao Hanidina !");
     Cockpit cockpit;
    while(window.isOpen())
    {
        sf::Event event;
        while(window.pollEvent(event))
        {
            if(event.type == sf::Event::Closed)
                window.close();
        }
        
        cockpit.draw(window);
        window.display();
    }

    return 0;
}