git pull

sbt10 compile

rm -rf ../gh-pages/*

cp target/scala-2.8.1.final/classes.sxr/* ../gh-pages/

cd ../gh-pages/

git pull

git add \*.html

git commit -a

