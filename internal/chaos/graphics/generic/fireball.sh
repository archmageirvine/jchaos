#!/bin/bash
#
# Build the fireball and similar image sets.
# Assumes ImageMagick is installed.

NAME=${1:-fireball}

# ${NAME}0.png is 256x256 with transparent background
# generate rotations
n=1
for theta in 30 60 90 120 150 180 210 240 270 300 330; do
    convert "${NAME}0.png" \( +clone -background none -rotate ${theta} \) -gravity center -compose Src -composite "${NAME}${n}.png"
    n=$((n+1))
done

# Create a mask for undrawing by overlaying all the images and then making
# the result slightly larger.
convert "${NAME}0.png" -threshold 0 -negate "${NAME}_mask.png"
for n in {1..11}; do
    convert "${NAME}${n}.png" -threshold 0 -negate "${NAME}_mask.png" -compose Dst_Over -composite "${NAME}_tmp.png" && mv "${NAME}_tmp.png" "${NAME}_mask.png"
done
convert "${NAME}_mask.png" -resize 272x272 -crop 256x256+8+8 "${NAME}_tmp.png" && mv "${NAME}_tmp.png" "${NAME}_mask.png"

# Scaled versions
DEST=../../../../src/chaos/graphics/
for px in 16 32 64; do
    for n in {0..11} _mask; do
        convert "${NAME}${n}.png" -resize "${px}x${px}" "${DEST}/active${px}/${NAME}${n}.png"
    done
done

# Clean up
/bin/rm "${NAME}"[1-9]*.png "${NAME}_mask.png"
