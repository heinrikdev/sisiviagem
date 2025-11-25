# SisViagem

SisViagem - Versão 2.0


## Backup e restore do banco do sisviagem

### Backup
```console
docker exec banco_sisviagem_prod /usr/bin/mysqldump -u root --password=***** __sw_sisviagem > backup.sql
```
### Restore
```console
cat backup.sql | docker exec -i banco_sisviagem_dev /usr/bin/mysql -u root --password=***** __sw_sisviagem
```

## Acesso para o Servidor HTTPS:

Acessar pelo [link disponível aqui](https://sisviagem.raiget.com/login), ou copiando o link abaixo:
```
https://sisviagem.raiget.com/login
```
