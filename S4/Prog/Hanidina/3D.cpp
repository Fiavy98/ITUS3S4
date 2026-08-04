#include <SFML/Window.hpp>
#include <SFML/OpenGL.hpp>
#include <GL/glu.h>
#include <cmath>

float yaw = 0.0f;    // gauche-droite
float pitch = 0.0f;  // haut-bas
float distance = 6.0f;

void drawMyObject()
{
    // ===============================
    // REMPLACER CETTE PARTIE
    // PAR TON IMAGE PNG PLUS TARD
    // ===============================

    glBegin(GL_QUADS);

    glColor3f(1,0,0);

    glVertex3f(-1,-1,0);
    glVertex3f( 1,-1,0);
    glVertex3f( 1, 1,0);
    glVertex3f(-1, 1,0);

    glEnd();
}

int main()
{
    sf::ContextSettings settings;
    settings.depthBits = 24;

    sf::Window window(
        sf::VideoMode(800,600),
        "Vue 3D",
        sf::Style::Default,
        settings
    );

    glEnable(GL_DEPTH_TEST);

    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    gluPerspective(60.0, 800.0/600.0, 1.0, 100.0);

    while(window.isOpen())
    {
        sf::Event event;

        while(window.pollEvent(event))
        {
            if(event.type == sf::Event::Closed)
                window.close();
        }

        if(sf::Keyboard::isKeyPressed(sf::Keyboard::Left))
            yaw -= 1.0f;

        if(sf::Keyboard::isKeyPressed(sf::Keyboard::Right))
            yaw += 1.0f;

        if(sf::Keyboard::isKeyPressed(sf::Keyboard::Up))
            pitch += 1.0f;

        if(sf::Keyboard::isKeyPressed(sf::Keyboard::Down))
            pitch -= 1.0f;

        float radYaw   = yaw * 3.14159f / 180.0f;
        float radPitch = pitch * 3.14159f / 180.0f;

        float camX = distance * cos(radPitch) * sin(radYaw);
        float camY = distance * sin(radPitch);
        float camZ = distance * cos(radPitch) * cos(radYaw);

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        // Caméra qui regarde toujours l'objet au centre
        gluLookAt(
            camX, camY, camZ,
            0, 0, 0,
            0, 1, 0
        );

        drawMyObject();

        window.display();
    }

    return 0;
}