#!/bin/bash

. "/opt/cbi4api/gbas/conf/setenv.sh"

APP_NAME="gbas-server"
export HBASE_HOME="$GBAS_HOME/apps/hbase-${org.apache.hbase.version}"
export HBASE_IDENT_STRING="gbas"
export HBASE_PID_DIR="$GBAS_HOME/var/hbase"
export HBASE_PID="$HBASE_PID_DIR/hbase-$HBASE_IDENT_STRING-master.pid"
export CATALINA_HOME="$GBAS_HOME/apps/apache-tomcat-${org.apache.tomcat.version}"
export CATALINA_BASE="$GBAS_HOME/apps/apache-tomcat-${org.apache.tomcat.version}"
export CATALINA_PID="$GBAS_HOME/var/tomcat/$APP_NAME.pid"

POST_SHUTDOWN_WAIT=5
POST_KILL15_WAIT=10
POST_KILL9_WAIT=10

cd $GBAS_HOME

exit_success () {
   curr_pid_mtime=`stat -c %Y $1 2>/dev/null`
   [ "$pid_mtime" == "$curr_pid_mtime" ] && rm -f $1
   if [ "$1" == "$CATALINA_PID" ]; then
      echo "$APP_NAME stopped OK"
      exit 0
   fi
}

shutdown() {
   for i in `seq $POST_SHUTDOWN_WAIT`; do
      if [ ! -d "/proc/$1" ] && exit_success $2; then
          return;
      fi
      sleep 1
   done

   echo "$APP_NAME process $1 still running, sending SIGTERM..."
   for i in `seq $POST_KILL15_WAIT`; do
      if [ ! -d "/proc/$1" ] && exit_success $2; then
         return;
      fi
      kill -15 $1
      sleep 1
   done

   echo "$APP_NAME process $1 still running, sending SIGKILL..."
   for i in `seq $POST_KILL9_WAIT`; do
      if [ ! -d "/proc/$1" ] && exit_success $2; then
         return;
      fi
      kill -9 $1
      sleep 1
   done

   echo "$APP_NAME shutdown FAILED, process $1 still running after kill -9. Manual intervention required!"
   exit 2
}


# check to see if thei HBASE process is already exited
PID=$(pgrep -f "Dhbase.home.dir=$HBASE_HOME")
if [ ! "$PID" = "" ] || ! exit_success $HBASE_PID ; then
   pid_mtime=`stat -c %Y $HBASE_PID 2>/dev/null`
   echo "$APP_NAME process $PID stopping..."
   "$HBASE_HOME/bin/stop-hbase.sh" #> /dev/null
   
   shutdown "$PID" "$HBASE_PID"
fi 

# check to see if the TOMCAT process is already exited
PID=$(pgrep -f "Dcatalina.base=$CATALINA_BASE")
[ "$PID" = "" ] && exit_success $CATALINA_PID 

pid_mtime=`stat -c %Y $CATALINA_PID 2>/dev/null`
"$CATALINA_HOME/bin/shutdown.sh" > /dev/null

shutdown "$PID" "$CATALINA_PID"


