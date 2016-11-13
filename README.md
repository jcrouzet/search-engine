# Description des dossier
Dans le dossier Data vous trouverez:
le corpus utilisé
les mots vides
fichier contenant chaque mot avec son identifiant dans l'index


Dans le dossier results_indexation

index tokenizer
indes avec stemming

Dans le dossier results

les resultat ( en cherchant dans l'index stemmer)
les resultat en cherchant dans index tokenzier

# Description des classes

Dans le dossier Index vous trouverez le code de generation d'index Index.java ainsi qu'in code pour donner à chaque fichier son identifiant dans l'index idFile.Java et enfin Encoding.java pour l'encodage à la base 62.

Dans le dossier corpus vous trouver un code corpus.Java  importer les fichier du dossier text qui nous interesse ( parsing xml)

Dans le dossier Search vous trouverer les classes qui permettent de chercher dans l'index et retourner les resultats les plus pertinents

Dans le dossier mains la classe main_stemmer pour lancer l'indexation stemming , main_tokenizer.java pour lancer l'indextion tokenzier ( pour generer les deux index il suffit d'executer le programme) Search_stemmer.java c'est pour qui permet de chercher dans notre moteur de recherche dans un index stemmer et token_serch.java qui permet de chercher dans l'index tokenizer.



