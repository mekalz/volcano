#!/usr/bin/env bash

WORKSPACE=$(cd $(dirname $0)/../; pwd)
cd $WORKSPACE

readonly MAIN=top.mekal.volcano.Runner
readonly CLASSPATH="${WORKSPACE}/conf:${WORKSPACE}/lib/*:${JAVA_HOME}/lib/*"
readonly JAVA="java"
readonly JVM_PERFORMANCE_OPTS="-server -XX:+UseG1GC -XX:+OptimizeStringConcat "
readonly GC_LOG_OPTS="-Xloggc:${WORKSPACE}/logs/gc.log -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:GCLogFileSize=30m -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=3"

readonly MEM_MAX=1G
readonly JVM_HEAP_OPTS="-Xms${MEM_MAX} -Xmx${MEM_MAX}"

function check_running() {
    ps aux | grep ${MAIN} | grep -v "grep" >/dev/null 2>&1
}

function start() {
    hash java 2>&- || { echo >&2 "I require java but it's not installed.  Aborting."; exit 1; }

    CONFPATH=${1:?"Config path should be specified!"}

    check_running
    running=$?
    if [ $running -eq 0 ];then
        echo "now is running already"
        return 1
    fi

    nohup ${JAVA} ${JVM_HEAP_OPTS} ${GC_LOG_OPTS} ${JVM_PERFORMANCE_OPTS} -cp ${CLASSPATH} ${MAIN} ${CONFPATH} &

    sleep 1

    check_running
    running=$?
    if [ ${running} -eq 0 ]; then
        echo "started"
    else
        echo "start failed"
    fi

    return ${running}
}

function stop() {
    ps aux | grep ${MAIN} | grep -v "grep" | awk '{print $2}' | xargs kill >/dev/null 2>&1
    return 0
}
