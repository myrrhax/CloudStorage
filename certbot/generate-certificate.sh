#!/bin/bash

rm -rf /etc/letsencrypt/live/certfolder*

certbot certonly --standalone --register-unsafely-without-email -d $DOMAIN_URL --cert-name=certfolder --key-type rsa --agree-tos

rm -rf /etc/nginx/cert.pem
rm -rf /etc/nginx/key.pem

cp /etc/letsencrypt/live/certfolder*/fullchain.pem /etc/nginx/cert.pem
cp /etc/letsencrypt/live/certfolder*/privatekey.pem /etc/nginx/key.pem