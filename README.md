# Projet TGPR 2223 - Gestion d'une banque

## Notes de version

### Liste des utilisateurs et mots de passes

  * admin@test.com, password "admin", administateur
  * ben@test.com, password "ben", manager
  * marc@test.com, password "marc", manager
  * bob@test.com, password "bob", client
  * caro@test.com, password "caro", client
  * louise@test.com, password "louise", client
  * jules@test.com, password "jules", clientw

### Liste des bugs connus

  * Manager: Problème dans  conditions dans la fonction delete et update dans UC_AccesClient.
  * Manager: Fonctionnalité delete un client dans le view EditClient ne fonctionne pas
  * TransferDetails: affichage de your account maglré que le compte n'est pas le compte utilisé
  * TransferDetails: affichage du saldo qui est toujours en positif
  * NewTransfer: possibilité d'ajouter une date antérieure à la date à laquelle l'utilisateur est connecté

### Liste des UC par membre du groupe:

* Kacar Senol: UC_Login, UC_History et UC_Manager(CRUDL clients de ses agences)
* Bersipont Amine: UC_BackToTheFuture et UC_TransferDetails
* Chaker Yacine: UC_FavouriteAccounts(Add,Edit) et UC_Manager(Affichage, création et suppression des accès)
* Houari Yassine: UC_ListCategory, UC_EditCategory et Manager(Access Manager)
* Aydogan Muhammed Mustafa: UC_AccountList et UC_NewTransfer



### Liste des fonctionnalités supplémentaires

Manager: modifier l'accès d'un compte pour un user et associer un client existant à un compte existant


