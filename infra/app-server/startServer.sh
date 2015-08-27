#! /bin/bash

set -e

# inspired by https://github.com/arun-gupta/docker-images/blob/master/wildfly-mysql-javaee7/customization/execute.sh
JBOSS_CLI=$JBOSS_HOME/bin/jboss-cli.sh

function wait_for_server() {
  until `$JBOSS_CLI -c ":read-attribute(name=server-state)" 2> /dev/null | grep -q running`; do
    sleep 1
  done
}


if [[ $# -lt 1 ]] || [[ "$1" == "--"* ]]; then

    # DB_SRV_PORT_5432_TCP_ADDR

    # Start WildFly in a different process for initial configuration
    echo "Starting WildFly for configuration"
    $JBOSS_HOME/bin/add-user.sh admin Admin#007 --silent
    $JBOSS_HOME/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0 > /dev/null &

    # Wait for WildFly to be ready
    echo "Waiting for WildFly to become ready"
    wait_for_server

    # Get the full path of the jdbc driver
    JAR=$(ls /opt/postgres*jar)
    # Prepare connection string
    CONNECTION_URL=jdbc:postgresql://$DB_SRV_PORT_5432_TCP_ADDR:$DB_SRV_PORT_5432_TCP_PORT/postgres

    # Check if postgres password is available from environment (source is the linked container)
    if [ -z "$DB_SRV_ENV_POSTGRES_PASSWORD" ]; then
        echo "[WARNING]: Postgres password not found in environment variable; falling back to default 'postgres'!"
        DB_SRV_ENV_POSTGRES_PASSWORD="postgres"
    fi

    echo "Driver jar: $JAR"
    echo "Connection url: $CONNECTION_URL"
    echo "Using password: $DB_SRV_ENV_POSTGRES_PASSWORD"

    # Execute JBOSS cli script to install driver and configure datasource
    echo "Installing driver and configuring data source"
    $JBOSS_CLI -c << EOF
    batch

    module add --name=org.postgres --resources=$JAR --dependencies=javax.api,javax.transaction.api
    /subsystem=datasources/jdbc-driver=postgres:add(driver-name="postgres",driver-module-name="org.postgres",driver-class-name=org.postgresql.Driver)

    data-source add --jndi-name=java:/PostGreDS --name=PostgrePool --connection-url=$CONNECTION_URL --driver-name=postgres --user-name=postgres --password=$DB_SRV_ENV_POSTGRES_PASSWORD

    run-batch
EOF

    # Deploy the WAR
    #echo "Deploying yatol war"
    #cp /opt/jboss/wildfly/customization/employees.war $JBOSS_HOME/$JBOSS_MODE/deployments/employees.war

    # Shut down WildFly process
    echo "Shutting down WildFly after configuration"
    $JBOSS_CLI -c ":shutdown"

    # Start WildFly with PID 1
    echo "Restarting WildFly as main process with PID 1"
    exec /bin/sh $JBOSS_HOME/bin/standalone.sh -b 0.0.0.0 "$@"
fi
exec "$@"
