#include "Cockpit.hpp"
#include <sstream>
#include <iomanip>

// ─────────────────────────────────────────────
//  Helper : formate sf::Time → "HH:MM:SS"
// ─────────────────────────────────────────────
std::string Cockpit::formatTime(sf::Time t) const
{
    int total   = static_cast<int>(t.asSeconds());
    int hours   = total / 3600;
    int minutes = (total % 3600) / 60;
    int seconds = total % 60;

    std::ostringstream ss;
    ss << std::setw(2) << std::setfill('0') << hours   << ":"
       << std::setw(2) << std::setfill('0') << minutes << ":"
       << std::setw(2) << std::setfill('0') << seconds;
    return ss.str();
}

// ─────────────────────────────────────────────
//  Helper : crée un bouton stylé
// ─────────────────────────────────────────────
void Cockpit::setupButton(sf::RectangleShape& btn, sf::Text& txt,
                          const std::string& label,
                          float x, float y, float w, float h)
{
    btn.setSize(sf::Vector2f(w, h));
    btn.setPosition(x, y);
    btn.setFillColor(sf::Color(180, 180, 180));
    btn.setOutlineThickness(2);
    btn.setOutlineColor(sf::Color::White);

    txt.setFont(font);
    txt.setString(label);
    txt.setCharacterSize(15);
    txt.setFillColor(sf::Color::Black);
    // Centrage approximatif dans le bouton
    txt.setPosition(x + 8, y + 8);
}

// ─────────────────────────────────────────────
//  Constructeur
// ─────────────────────────────────────────────
Cockpit::Cockpit()
    // Instruments vitesse  (x, y)
    : Vx   (30,  360),
      Vy   (200, 360),
      Vtot (370, 360),
      // Compteur altitude/distance  — bande juste en dessous des jauges
      compteur(0, 560, 890, 40),
      // Barre info décrochage       — bande la plus basse du dashboard gauche
      infoBar (0, 600, 890, 40),
      // Freinage X et Y (côté droit du dashboard)
      freinageX("Freinage x", 910,  430, 190, 110),
      freinageY("Freinage y", 1110, 430, 190, 110),
      chronoPaused(false),
      chronoElapsed(sf::Time::Zero)
{
    // ── Pare-brise ─────────────────────────────
    windshield.setSize(sf::Vector2f(1600, 350));
    windshield.setPosition(0, 0);
    windshield.setFillColor(sf::Color(100, 180, 255));

    // ── Nuage animé ────────────────────────────
    cloud.setSize(sf::Vector2f(150, 50));
    cloud.setPosition(1400, 100);
    cloud.setFillColor(sf::Color::White);

    // ── Dashboard ──────────────────────────────
    dashboard.setSize(sf::Vector2f(1600, 550));
    dashboard.setPosition(0, 350);
    dashboard.setFillColor(sf::Color(70, 70, 70));
    dashboard.setOutlineThickness(2);
    dashboard.setOutlineColor(sf::Color::White);

    // ── Chronomètre (centre haut du dashboard) ─
    //   Image : "00:00:30" affiché en cyan, fond sombre, centré ~x=620
    chronoBackground.setSize(sf::Vector2f(200, 45));
    chronoBackground.setPosition(600, 355);
    chronoBackground.setFillColor(sf::Color(20, 20, 20));
    chronoBackground.setOutlineThickness(2);
    chronoBackground.setOutlineColor(sf::Color(0, 180, 180));

    chronoText.setFont(font);
    chronoText.setString("00:00:00");
    chronoText.setCharacterSize(24);
    chronoText.setFillColor(sf::Color::Cyan);
    chronoText.setPosition(612, 360);

    // ── Bouton Pause (à droite du chrono) ──────
    //   Image : cercle hexagonal avec triangle ▷
    btnPause.setSize(sf::Vector2f(50, 45));
    btnPause.setPosition(815, 355);
    btnPause.setFillColor(sf::Color(40, 60, 80));
    btnPause.setOutlineThickness(2);
    btnPause.setOutlineColor(sf::Color(0, 160, 200));

    txtPause.setFont(font);
    txtPause.setString("||");
    txtPause.setCharacterSize(20);
    txtPause.setFillColor(sf::Color(0, 200, 255));
    txtPause.setPosition(824, 362);

    // ── Boutons de direction (droite du dashboard) ─
    //   Image : Avant / Gauche / Droite / Arrière / Bas
    //   Disposition : Avant seul en haut, G/D au milieu, Arrière+Bas en bas
    setupButton(btnAvant,   txtAvant,   "Avant",   990, 365);
    setupButton(btnGauche,  txtGauche,  "Gauche",  895, 410);
    setupButton(btnDroite,  txtDroite,  "Droite",  1095, 410);
    setupButton(btnArriere, txtArriere, "Arriere", 990, 410);
    setupButton(btnBas,     txtBas,     "Bas",     1195, 410, 70, 35);
}

// ─────────────────────────────────────────────
//  Gestion des évènements
// ─────────────────────────────────────────────
void Cockpit::handleEvent(const sf::Event& event, sf::RenderWindow& window)
{
    // Pause du chronomètre
    if (event.type == sf::Event::MouseButtonPressed &&
        event.mouseButton.button == sf::Mouse::Left)
    {
        sf::Vector2f mousePos(
            static_cast<float>(event.mouseButton.x),
            static_cast<float>(event.mouseButton.y)
        );

        if (btnPause.getGlobalBounds().contains(mousePos))
        {
            if (chronoPaused)
            {
                // Reprendre : relancer l'horloge
                chronoClock.restart();
                chronoPaused = false;
                txtPause.setString("||");
            }
            else
            {
                // Pause : accumuler le temps écoulé
                chronoElapsed += chronoClock.getElapsedTime();
                chronoPaused = true;
                txtPause.setString(">");
            }
        }
    }

    // Déléguer les clics aux Freinages
    freinageX.handleEvent(event, window);
    freinageY.handleEvent(event, window);
}

// ─────────────────────────────────────────────
//  Mise à jour
// ─────────────────────────────────────────────
void Cockpit::update()
{
    // Nuage
    cloud.move(-0.3f, 0);
    if (cloud.getPosition().x < -150)
        cloud.setPosition(1600, cloud.getPosition().y);

    // Chronomètre
    sf::Time displayed = chronoElapsed;
    if (!chronoPaused)
        displayed += chronoClock.getElapsedTime();
    chronoText.setString(formatTime(displayed));
}

// ─────────────────────────────────────────────
//  Dessin
// ─────────────────────────────────────────────
void Cockpit::draw(sf::RenderWindow& window)
{
    // Arrière-plan
    window.draw(windshield);
    window.draw(cloud);
    window.draw(dashboard);

    // Chronomètre + pause
    window.draw(chronoBackground);
    window.draw(chronoText);
    window.draw(btnPause);
    window.draw(txtPause);

    // Instruments de vitesse
    Vx.draw(window);
    Vy.draw(window);
    Vtot.draw(window);

    // Compteur altitude / distance piste
    compteur.draw(window);

    // Barre info décrochage
    infoBar.draw(window);

    // Freinage
    freinageX.draw(window);
    freinageY.draw(window);

    // Boutons de direction
    window.draw(btnAvant);   window.draw(txtAvant);
    window.draw(btnGauche);  window.draw(txtGauche);
    window.draw(btnDroite);  window.draw(txtDroite);
    window.draw(btnArriere); window.draw(txtArriere);
    window.draw(btnBas);     window.draw(txtBas);
}