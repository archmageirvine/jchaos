#!/bin/bash
#
# Construct the javadocs for the source hierarchy.

# generate image for common hierarchy
export PATH=$PATH:/usr/local/share/gv1.5/bin
cat >dotty.dot <<EOF
digraph castable {
Castable;
FreeCastable;
FreePowerUp;
Actor;
MaterialGrowth;
Monster;
AbstractGenerator;
MaterialMonster;
Dragon;
Caster;
UndeadMonster;
Wizard;
Unicaster;
Polycaster;
Castable->FreeCastable;
Castable->Actor;
Actor->MaterialGrowth;
Actor->Monster;
Actor->AbstractGenerator;
Monster->MaterialMonster;
Monster->Dragon;
Monster->Caster;
Monster->UndeadMonster;
Caster->Unicaster;
Caster->Polycaster;
Caster->Wizard;
FreeCastable->FreePowerUp;
}
EOF
dot -o Castable.ps -Tps dotty.dot
pstoimg -crop a -scale .8 Castable.ps
mv Castable.* chaos/common/doc-files
/bin/rm -rf dotty.dot

# copy in some graphs for growths
if [ ! -r sundry/coverage.gif ]; then
    cd sundry; growthanalysis.sh
fi
cp sundry/coverage.gif sundry/death.gif sundry/count.gif chaos/common/doc-files

# do the java docs
files=$(find . -name "*.java" | sed "s/^.\\///g")
packages=$(for f in ${files}; do echo "${f%/*}"; done | sort | uniq | tr '/' '.' | grep -v java | tr ' ' ':')
javadoc -source 1.4 -J-mx100m -public -author -version -windowtitle jChaos -splitindex -d ../javadocs/ -subpackages "${packages}"

