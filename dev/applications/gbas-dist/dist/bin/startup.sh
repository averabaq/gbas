#!/bin/bash

. "/opt/cbi4api/gbas/conf/setenv.sh"

APP_NAME="gbas-server"
export JAVA_OPTS="-server -Xms512m -Xmx1024m -XX:+AggressiveHeap -XX:MaxPermSize=512m -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5050 -Dcom.sun.management.jmxremote.port=21113 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Dconfig=file://$GBAS_HOME/etc/gbas.properties -Dspring.profiles.active=default"
export HBASE_HOME="$GBAS_HOME/apps/hbase-${org.apache.hbase.version}"
export HBASE_CONF_DIR="$HBASE_HOME/conf"
export HBASE_LOG_DIR="$HBASE_HOME/logs"
export HBASE_IDENT_STRING="gbas"
export HBASE_PID_DIR="$GBAS_HOME/var/hbase"
export HBASE_PID="$HBASE_PID_DIR/hbase-$HBASE_IDENT_STRING-master.pid"
export CATALINA_HOME="$GBAS_HOME/apps/apache-tomcat-${org.apache.tomcat.version}"
export CATALINA_BASE="$GBAS_HOME/apps/apache-tomcat-${org.apache.tomcat.version}"
export CATALINA_PID="$GBAS_HOME/var/tomcat/$APP_NAME-tomcat.pid"
export CATALINA_OPTS="-Djava.security.egd=file:/dev/urandom -Djmagick.systemclassloader=no "
export LANG=en_GB.UTF-8
GRACEPERIOD=4
STARTUP_WAIT=30

if [ "`id -u`" == "0" ]; then
    echo "$APP_NAME: startup.sh called as root, ABORTING" 1>&2
    exit 1
fi

cd $GBAS_HOME

# exit if there is any existing running server or component
PID=$(pgrep -f "Dcatalina.base=$CATALINA_BASE")
if [ "$PID" != "" ] ; then
    echo "$APP_NAME: already running, not restarting"
    exit 0
fi

# exit if there is any existing hbase instance running
PID=$(pgrep -f "Dhbase.home.dir=$HBASE_HOME")
if [ "$PID" != "" ] ; then
    echo "$APP_NAME: already running, not restarting"
    exit 0
fi

# ensure this doesn't exist until the server is started ok
rm -f $CATALINA_PID || true
rm -f $HBASE_PID || true

echo "$APP_NAME: starting..."

# HBASE
#echo "Starting HBASE..."
nohup "$HBASE_HOME/bin/start-hbase.sh" < /dev/null > /dev/null 2>&1

sleep $GRACEPERIOD

PID=`cat $HBASE_PID`
HBASE_LOGS="$HBASE_HOME/logs/hbase-$HBASE_IDENT_STRING-master-$HOSTNAME.log"

if [ "$PID" = "" -o ! -d "/proc/$PID" ]; then
    echo "$APP_NAME: failed to start"
    echo "HBASE logs, from tail $HBASE_LOGS:"
    tail "$HBASE_LOGS"
    exit 1
fi

for i in `seq $STARTUP_WAIT`; do
    [ ! -d "/proc/$PID/" ] && break
    if grep -q "$HBASE_HOME" /proc/$PID/cmdline > /dev/null 2>&1 ; then
        #echo "HBASE process $PID: started"
        break;
    else 
      echo "$APP_NAME process $PID: FAILED to start (see $HBASE_LOGS)"
      exit 1
    fi 
    sleep 1
done

# TOMCAT
#echo "Starting apache tomcat..."
nohup "$CATALINA_HOME/bin/startup.sh" < /dev/null > /dev/null 2>&1

sleep $GRACEPERIOD

PID=`cat $CATALINA_PID`

if [ "$PID" = "" -o ! -d "/proc/$PID" ]; then
    echo "$APP_NAME: failed to start"
    echo "Tomcat logs, from tail logs/catalina.out:"
    tail "$CATALINA_HOME/logs/catalina.out"
    exit 1
fi

for i in `seq $STARTUP_WAIT`; do
    [ ! -d "/proc/$PID/" ] && break
    if grep -q "$CATALINA_BASE" /proc/$PID/cmdline > /dev/null 2>&1 ; then
        echo "$APP_NAME process $PID: started"
        exit 0
    fi
    sleep 1
done

echo "$APP_NAME process $PID: FAILED to start (see $GBAS_HOME/logs/catalina.out)"
exit 1

