#include "Cockpit.hpp"
#include <iomanip>
#include <sstream>

Cockpit::Cockpit()
    : pareBrise(1600.f, 350.f),   // ← AJOUTER ICI
      Vx(20, 340),
      Vy(240, 340),
      Vtot(460, 340),
      altiude("Altitude", 30, 530),
      distPiste("Distance Piste", 260, 530),
      Vcrochage("V decrochage", 30, 610),
      limiteX("lim X", 260, 610),
      limiteY("lim Y", 490, 610),
      freinageX("Freinage x", 900, 530),
      freinageY("Freinage y", 1060, 530)
{
    // Dashboard
    dashboard.setSize(sf::Vector2f(1600, 450));
    dashboard.setPosition(0, 350);
    dashboard.setFillColor(sf::Color(50, 50, 50));
    dashboard.setOutlineThickness(2);
    dashboard.setOutlineColor(sf::Color::White);

    police.loadFromFile("assets/fonts/arial.ttf");

    // Vitesses
    Vx.setFont(police);
    Vy.setFont(police);
    Vtot.setFont(police);
    Vx.setSpeed(0.f);
    Vy.setSpeed(0.f);
    Vtot.setSpeed(0.f);

    estEnPause = false;
    tempsEcoule = sf::Time::Zero;

    // Boutons direction (inchangés)
    sf::Vector2f tailleBtn(80, 30);
    btnAvant.setSize(tailleBtn);
    btnArriere.setSize(tailleBtn);
    btnGauche.setSize(tailleBtn);
    btnDroite.setSize(tailleBtn);
    btnArriereCentre.setSize(tailleBtn);

    btnAvant.setPosition(1000, 360);
    btnGauche.setPosition(900, 420);
    btnArriereCentre.setPosition(1000, 420);
    btnDroite.setPosition(1100, 420);
    btnArriere.setPosition(1000, 480);

    sf::Color couleurBtn(180, 180, 180);
    btnAvant.setFillColor(couleurBtn);
    btnArriere.setFillColor(couleurBtn);
    btnGauche.setFillColor(couleurBtn);
    btnDroite.setFillColor(couleurBtn);
    btnArriereCentre.setFillColor(couleurBtn);

    auto configTexte = [this](sf::Text& t, const std::string& str, const sf::RectangleShape& btn) {
        t.setFont(police);
        t.setString(str);
        t.setCharacterSize(14);
        t.setFillColor(sf::Color::Black);
        sf::FloatRect bounds = t.getLocalBounds();
        t.setPosition(
            btn.getPosition().x + (btn.getSize().x - bounds.width) / 2.f - bounds.left,
            btn.getPosition().y + (btn.getSize().y - bounds.height) / 2.f - bounds.top
        );
    };

    configTexte(txtAvant,        "Avant",   btnAvant);
    configTexte(txtGauche,       "Gauche",  btnGauche);
    configTexte(txtArriereCentre,"Arriere", btnArriereCentre);
    configTexte(txtDroite,       "Droite",  btnDroite);
    configTexte(txtArriere,      "Bas",     btnArriere);

    txtChrono.setFont(police);
    txtChrono.setCharacterSize(26);
    txtChrono.setFillColor(sf::Color(0, 255, 255));
    txtChrono.setPosition(730, 360);

    btnPause.setSize(sf::Vector2f(50, 35));
    btnPause.setPosition(870, 360);
    btnPause.setFillColor(sf::Color(70, 70, 70));
    btnPause.setOutlineThickness(1);
    btnPause.setOutlineColor(sf::Color::White);

    txtPause.setFont(police);
    txtPause.setString("II");
    txtPause.setCharacterSize(18);
    txtPause.setFillColor(sf::Color::White);
    sf::FloatRect pBounds = txtPause.getLocalBounds();
    txtPause.setPosition(
        btnPause.getPosition().x + (btnPause.getSize().x - pBounds.width) / 2.f - pBounds.left,
        btnPause.getPosition().y + (btnPause.getSize().y - pBounds.height) / 2.f - pBounds.top
    );
}

void Cockpit::update()
{
    if(!estEnPause)
        tempsEcoule += horloge.restart();
    else
        horloge.restart();

    int totalSecondes = static_cast<int>(tempsEcoule.asSeconds());
    int heures   = totalSecondes / 3600;
    int minutes  = (totalSecondes % 3600) / 60;
    int secondes = totalSecondes % 60;

    std::stringstream ss;
    ss << std::setfill('0')
       << std::setw(2) << heures   << ":"
       << std::setw(2) << minutes  << ":"
       << std::setw(2) << secondes;
    txtChrono.setString(ss.str());

    pareBrise.update(estEnPause);  // ← remplace cloud.move()
}

void Cockpit::updateVitese(float vx_ms, float vy_ms, float vtot_ms)
{
    Vx.setSpeed(vx_ms   * 3.6f);
    Vy.setSpeed(vy_ms   * 3.6f);
    Vtot.setSpeed(vtot_ms * 3.6f);
}

void Cockpit::setVy(float vy)
{
    pareBrise.setVy(vy);  // transmet Vy aux nuages
}

void Cockpit::gererClic(sf::Vector2i positionSouris, sf::RenderWindow& window)
{
    sf::Vector2f coord = window.mapPixelToCoords(positionSouris);

    // Pause
    if(btnPause.getGlobalBounds().contains(coord)) {
        estEnPause = !estEnPause;
        if(estEnPause) {
            txtPause.setString("|>");
            btnPause.setFillColor(sf::Color(100, 50, 50));
        } else {
            txtPause.setString("II");
            btnPause.setFillColor(sf::Color(70, 70, 70));
        }
        sf::FloatRect pBounds = txtPause.getLocalBounds();
        txtPause.setPosition(
            btnPause.getPosition().x + (btnPause.getSize().x - pBounds.width) / 2.f - pBounds.left,
            btnPause.getPosition().y + (btnPause.getSize().y - pBounds.height) / 2.f - pBounds.top
        );
    }

    // Changement de vue pare-brise
    if(btnAvant.getGlobalBounds().contains(coord))
        pareBrise.setVue(Direction::AVANT);
    if(btnGauche.getGlobalBounds().contains(coord))
        pareBrise.setVue(Direction::GAUCHE);
    if(btnDroite.getGlobalBounds().contains(coord))
        pareBrise.setVue(Direction::DROITE);
    if(btnArriereCentre.getGlobalBounds().contains(coord))
        pareBrise.setVue(Direction::ARRIERE);
    if(btnArriere.getGlobalBounds().contains(coord))
        pareBrise.setVue(Direction::BAS);
}

void Cockpit::draw(sf::RenderWindow& window)
{
    pareBrise.draw(window);      // ← remplace windshield + cloud
    window.draw(dashboard);

    Vx.draw(window);
    Vy.draw(window);
    Vtot.draw(window);

    altiude.draw(window);
    distPiste.draw(window);
    Vcrochage.draw(window);
    limiteX.draw(window);
    limiteY.draw(window);
    freinageX.draw(window);
    freinageY.draw(window);

    window.draw(btnAvant);
    window.draw(btnGauche);
    window.draw(btnArriereCentre);
    window.draw(btnDroite);
    window.draw(btnArriere);

    window.draw(txtAvant);
    window.draw(txtGauche);
    window.draw(txtArriereCentre);
    window.draw(txtDroite);
    window.draw(txtArriere);

    window.draw(txtChrono);
    window.draw(btnPause);
    window.draw(txtPause);
}