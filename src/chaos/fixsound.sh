#!/bin/bash
#
# Make Unix links for appropriate sounds
cd resources/sound

for file in $(grep -l -r TEXTLINK ./* | tr ' ' '_'); do
    f="$(echo ${file} | tr '_' ' ')"
    dest=$(tail -1 "$f" | gawk -F'"' '{print $2}')
    /bin/rm "$f"
    ln -s "$dest" "$f"
done

mv "Combat/Iridium Horse" Combat/Iridium
mv Combat/Devil "Combat/Higher Devil"
ln -s "Combat/Higher Devil" "Combat/Lesser Devil"

cd -
