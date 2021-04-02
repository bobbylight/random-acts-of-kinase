#!/bin/bash
# Not sure why this is needed, but nginx shows the default "upload your app"
# landing page without a post-deploy restart.
sudo systemctl restart nginx
