# SpaceExplorers
Multiple Producer, Multiple Consumer in Java

HeadQuarters si SpaceExplorer tin atat loc de consumator, cat si de producator.
Space Explorers trebuie sa descopere noi galaxii (sub forma de graf) si sa trimita
o anumita frecventa a unei noi galaxii catre HeadQuarters, care vor decodifica
frecventa si le vor trimite rezultatul, pentru a putea marca acea galaxie ca
"descoperita".

Asadar, se vor folosi doua canale de comunicare (resurse partajate), unul de la
SpaceExplorer la HeadQuarter, altul de la HeadQuarter la SpaceExplorer.
