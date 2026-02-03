# Database Migrations - Flyway

## Estrutura de Diretórios

```
src/main/resources/db/migration/
├── V1__create_initial_schema.sql
├── V2__insert_default_profiles.sql
└── V{version}__{description}.sql
```

## Schema Atual

### Tabelas
- **profile** - Perfis de usuário (CLIENT, OWNER)
- **users** - Usuários do sistema (UUID)
- **address** - Endereços dos usuários (UUID)
- **user_profile** - Relacionamento many-to-many entre users e profile
- **restaurant** - Restaurantes (UUID)
- **food_item** - Itens do cardápio (UUID)

### Características
- IDs: UUID (gen_random_uuid()) exceto profile (INTEGER)
- Timestamps: TIMESTAMPTZ para suporte a timezone
- Relacionamentos: Many-to-many entre users e profile
- Cascata: DELETE CASCADE em address e food_item

## Convenção de Nomenclatura

As migrations seguem o padrão Flyway:

- **V** - Prefixo obrigatório para migrations versionadas
- **{version}** - Número da versão (ex: 1, 2, 3, 1.1, 2.1)
- **__** - Dois underscores separando versão da descrição
- **{description}** - Descrição em snake_case
- **.sql** - Extensão do arquivo

### Exemplos:
- `V1__create_initial_schema.sql`
- `V2__insert_default_profiles.sql`
- `V3__add_restaurant_table.sql`
- `V4__alter_user_add_phone.sql`

## Como Criar uma Nova Migration

1. Crie um arquivo SQL em `src/main/resources/db/migration/`
2. Nomeie seguindo o padrão: `V{próximo_número}__{descrição}.sql`
3. Escreva o SQL DDL/DML necessário
4. Reinicie a aplicação - Flyway executará automaticamente

### Exemplo:

```sql
-- V3__add_reservation_table.sql
CREATE TABLE reservation (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    restaurant_id UUID NOT NULL,
    user_id UUID NOT NULL,
    reservation_date TIMESTAMPTZ NOT NULL,
    number_of_people INTEGER NOT NULL,
    status VARCHAR(50) NOT NULL,
    date_created TIMESTAMPTZ NOT NULL,
    last_updated TIMESTAMPTZ NOT NULL,
    CONSTRAINT fk_reservation_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurant(id),
    CONSTRAINT fk_reservation_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE INDEX idx_reservation_restaurant_id ON reservation(restaurant_id);
CREATE INDEX idx_reservation_user_id ON reservation(user_id);
```

## Boas Práticas

1. **Nunca modifique migrations já aplicadas** - Crie uma nova migration
2. **Uma migration = Uma responsabilidade** - Não misture alterações não relacionadas
3. **Sempre teste localmente** antes de commitar
4. **Use transações implícitas** - Flyway gerencia automaticamente
5. **Migrations devem ser idempotentes quando possível**
6. **Documente alterações complexas** com comentários SQL

## Comandos Úteis

### Verificar status das migrations:
```bash
docker compose exec app ./mvnw flyway:info
```

### Validar migrations:
```bash
docker compose exec app ./mvnw flyway:validate
```

### Reparar histórico (use com cuidado):
```bash
docker compose exec app ./mvnw flyway:repair
```

## Configuração

As configurações do Flyway estão em `application.properties`:

```properties
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
spring.flyway.locations=classpath:db/migration
spring.flyway.validate-on-migrate=true
spring.flyway.out-of-order=false
```

## Tabela de Controle

Flyway cria automaticamente a tabela `flyway_schema_history` para rastrear migrations aplicadas.

**Nunca modifique esta tabela manualmente!**

## Rollback

Flyway Community Edition não suporta rollback automático. Para reverter:

1. Crie uma nova migration que desfaça as alterações
2. Exemplo: Se `V3__add_column.sql` adiciona uma coluna, crie `V4__remove_column.sql`

## Troubleshooting

### Migration falhou:
1. Verifique os logs da aplicação
2. Execute `flyway:repair` se necessário
3. Corrija o problema
4. Reinicie a aplicação

### Conflito de versão:
- Certifique-se de que não há migrations com o mesmo número de versão
- Use versionamento sequencial

### Schema desatualizado:
- Flyway detecta automaticamente e aplica migrations pendentes no startup
