#! /bin/bash

set -e


if [[ $# -lt 1 ]] || [[ "$1" == "--"* ]]; then
   exec /bin/sh /opt/jboss/wildfly/bin/standalone.sh -b 0.0.0.0 "$@"
fi
exec "$@"
