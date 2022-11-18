# Projet TGPR 2223 - Gestion d'une banque

## Notes de version

### Liste des utilisateurs et mots de passes

  * admin@test.com, password "admin", administateur
  * ben@test.com, password "ben", manager
  * marc@test.com, password "marc", manager
  * bob@test.com, password "bob", client
  * caro@test.com, password "caro", client
  * louise@test.com, password "louise", client
  * jules@test.com, password "jules", client

### Liste des bugs connus

  * Manager: Dans la page où on donne l'accès à un compte pour un client, on ne sait pas update son accès
  * Manager: Fonctionnalité delete un client dans le view EditClient ne fonctionne pas
  * History: filtrage de la date qui filtre mal et qui compare avec le format de la base de données
  * TransferDetails: affichage de your account maglré que le compte n'est pas le compte utilisé
  * TransferDetails: affichage du saldo qui est toujours en positif
  * TransferDetails: affichage de effective at qui doit s'afficher sans l'heure

### Liste des UC par membre du groupe:

* Kacar Senol: UC_Login, UC_History et UC_Manager(CRUDL clients de ses agences)
* Bersipont Amine: UC_BackToTheFuture et UC_TransferDetails
* Chaker Yacine: UC_FavouriteAccounts(Add,Edit) et UC_Manager(Affichage, création et suppression des accès)
* Houari Yassine: UC_ListCategory, UC_EditCategory et Manager(Access Manager)
* Aydogan Muhammed Mustafa: UC_AccountList et UC_NewTransfer



### Liste des fonctionnalités supplémentaires

Manager: modifier l'accès d'un compte pour un user et associer un client existant à un compte existant


