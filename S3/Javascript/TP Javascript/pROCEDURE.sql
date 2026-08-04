CREATE OR REPLACE PROCEDURE My_proc_return_film(p_rental_id INT)
LANGUAGE plpgsql
AS $$
DECLARE
    v_rental_date DATE;
    v_inventory_id INT;
    v_customer_id INT;
    v_return_date DATE;
    v_staff_id INT;
    v_last_update DATE;
    v_retard INT;
    v_penalty NUMERIC(6,2);



    --1. Vérifier que le rental existe
        SELECT r.rental_date, r.return_date, r.customer_id, r.staff_id,
        f.rental_duration, f.title, f.rental_rate
    INTO v_rental_date, v_return_date, v_customer_id, v_staff_id,
         v_rental_duration, v_film_title, v_rental_rate
    FROM rental r
    JOIN inventory i ON r.inventory_id = i.inventory_id
    JOIN film f ON i.film_id = f.film_id
    WHERE r.rental_id = p_rental_id;  

    IF NOT FOUND THEN
        RAISE EXCEPTION 'Client % inexistant',p_rental_id;
    END IF;

    --2. Calculer automatiquement le retard (return_date - rental_date)
    v_retard := GREATEST(EXTRACT(DAY FROM (NOW() - v_rental_date))::INT - v_rental_duration, 0);

    --3.Mettre à jour return_date
    UPDATE rental
    SET return_date=NOW()
    WHERE rental_id = p_rental_id;

    --4.. Si retard > 0 :
    --• calculer une pénalité
    v_penalty := LEAST(v_retard * v_rental_rate * 0.10, 999.99);
               --insérer cette pénalité dans payment
        INSERT INTO payment(customer_id, staff_id, amount, payment_date, rental_id)
        VALUES (v_customer_id, v_staff_id, v_penalty, NOW(), p_rental_id);
    END IF;

    --Affichage NOTICE
    RAISE NOTICE 'Film "%" rendu. Durée de location : % jours. Retard : % jours. Pénalité : %',
                 v_film_title, v_rental_duration, v_retard, v_penalty;
END;
$$;