#!/bin/bash
#
# Update the 16x16 and 64x64 image based on the 32x32 image for a particular tile.

tile=$1
if [[ -z ${tile} ]]; then
    echo "Usage: $0 tile-number"
    return
fi

java irvine.tile.TileSet "${tile}" "${tile}.png" 5
pngtopnm <"${tile}".png | pnmscale -w 16 -h 16 | pnmtopng >"${tile}-16.png"
pngtopnm <"${tile}".png | pnmscale -w 64 -h 64 | pnmtopng >"${tile}-64.png"
java irvine.tile.TileSet -s "${tile}" "${tile}-16.png" 4
java irvine.tile.TileSet -s "${tile}" "${tile}-64.png" 6
