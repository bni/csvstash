#!/bin/sh
HEADER=$(cat <<'EOF'
#!/bin/sh
MYSELF=`which "$0" 2>/dev/null`
[ $? -gt 0 -a -f "$0" ] && MYSELF="./$0"
java=java
if test -n "$JAVA_HOME"; then
    java="$JAVA_HOME/bin/java"
fi
exec "$java" $java_args -jar $MYSELF "$@"
exit 1
EOF
)

echo "$HEADER" | cat - \
target/csvstash-1.0-SNAPSHOT-jar-with-dependencies.jar > \
target/csvstash && \
chmod +x target/csvstash
