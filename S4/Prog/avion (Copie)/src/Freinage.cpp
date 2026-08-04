#include "Freinage.hpp"

Freinage::Freinage(const std::string& name, float x, float y, float width, float height, float step)
    : value(0.f), step(step)
{
    // Fond du bloc
    background.setSize(sf::Vector2f(width, height));
    background.setPosition(x, y);
    background.setFillColor(sf::Color(50, 50, 50));
    background.setOutlineThickness(2);
    background.setOutlineColor(sf::Color(120, 120, 120));

    // Label titre (ex: "Freinage x")
    label.setFont(font);
    label.setString(name);
    label.setCharacterSize(16);
    label.setFillColor(sf::Color::White);
    // Centré en haut du bloc
    label.setPosition(x + 10, y + 8);

    // Bouton +
    float btnSize = 40.f;
    float btnY = y + height - btnSize - 10.f;

    btnPlus.setSize(sf::Vector2f(btnSize, btnSize));
    btnPlus.setPosition(x + 10, btnY);
    btnPlus.setFillColor(sf::Color(80, 80, 80));
    btnPlus.setOutlineThickness(2);
    btnPlus.setOutlineColor(sf::Color::White);

    txtPlus.setFont(font);
    txtPlus.setString("+");
    txtPlus.setCharacterSize(22);
    txtPlus.setFillColor(sf::Color::White);
    txtPlus.setPosition(x + 20, btnY + 4);

    // Bouton -
    btnMoins.setSize(sf::Vector2f(btnSize, btnSize));
    btnMoins.setPosition(x + btnSize + 20, btnY);
    btnMoins.setFillColor(sf::Color(80, 80, 80));
    btnMoins.setOutlineThickness(2);
    btnMoins.setOutlineColor(sf::Color::White);

    txtMoins.setFont(font);
    txtMoins.setString("-");
    txtMoins.setCharacterSize(22);
    txtMoins.setFillColor(sf::Color::White);
    txtMoins.setPosition(x + btnSize + 32, btnY + 4);
}

void Freinage::setOnChange(std::function<void(float)> callback)
{
    onChange = callback;
}

float Freinage::getValue() const { return value; }

void Freinage::setValue(float val)
{
    value = val;
    if (onChange) onChange(value);
}

void Freinage::handleEvent(const sf::Event& event, sf::RenderWindow& window)
{
    if (event.type == sf::Event::MouseButtonPressed &&
        event.mouseButton.button == sf::Mouse::Left)
    {
        sf::Vector2f mousePos(
            static_cast<float>(event.mouseButton.x),
            static_cast<float>(event.mouseButton.y)
        );

        if (btnPlus.getGlobalBounds().contains(mousePos))
        {
            value += step;
            if (onChange) onChange(value);
        }
        else if (btnMoins.getGlobalBounds().contains(mousePos))
        {
            value -= step;
            if (onChange) onChange(value);
        }
    }
}

void Freinage::draw(sf::RenderWindow& window)
{
    window.draw(background);
    window.draw(label);
    window.draw(btnPlus);
    window.draw(txtPlus);
    window.draw(btnMoins);
    window.draw(txtMoins);
}