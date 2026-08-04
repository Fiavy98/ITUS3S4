<?php

	sleep(5);
  header( "Content-Type: application/json"); 

  $retour = array(
  				0 => array("Nom"=>"Rakoto", "Prenom"=>"John", "AnneeNaissance"=>1990),
  				1 => array("Nom"=>"Rasoa", "Prenom"=>"Kininina", "AnneeNaissance"=>1994),
  				2 => array("Nom"=>"Rabe", "Prenom"=>"Jean", "AnneeNaissance"=>1993)
  			); 
			sleep(10);

   echo json_encode($retour);
  

?>