CREATE USER IF NOT EXISTS 'galsie'@'localhost' IDENTIFIED BY 'galsie123';

CREATE DATABASE IF NOT EXISTS galEmailDb;
GRANT ALL PRIVILEGES ON galEmailDb.* TO 'galsie'@'localhost';

CREATE DATABASE IF NOT EXISTS galGCSSentryDB;
GRANT ALL PRIVILEGES ON galGCSSentryDB.* TO 'galsie'@'localhost';

CREATE DATABASE IF NOT EXISTS galCertAuthDb;
GRANT ALL PRIVILEGES ON galCertAuthDb.* TO 'galsie'@'localhost';

CREATE DATABASE IF NOT EXISTS galContinuousDb;
GRANT ALL PRIVILEGES ON galContinuousDb.* TO 'galsie'@'localhost';

CREATE DATABASE IF NOT EXISTS galHomesDb;
GRANT ALL PRIVILEGES ON galHomesDb.* TO 'galsie'@'localhost';

CREATE DATABASE IF NOT EXISTS galHomesDataWarehouseDb;
GRANT ALL PRIVILEGES ON galHomesDataWarehouseDb.* TO 'galsie'@'localhost';

CREATE DATABASE IF NOT EXISTS galUsersDb;
GRANT ALL PRIVILEGES ON galUsersDb.* TO 'galsie'@'localhost';

CREATE DATABASE IF NOT EXISTS galResourcesDb;
GRANT ALL PRIVILEGES ON galResourcesDb.* TO 'galsie'@'localhost';

CREATE DATABASE IF NOT EXISTS galTestDb;
GRANT ALL PRIVILEGES ON galTestDb.* TO 'galsie'@'localhost';

CREATE DATABASE IF NOT EXISTS galSmsDb;
GRANT ALL PRIVILEGES ON galSmsDb.* TO 'galsie'@'localhost';

FLUSH PRIVILEGES;
