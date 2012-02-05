sbt11 clean test:compile

cd ../gh-pages

git pull origin gh-pages

rm -rf ./main/*
rm -rf ./test/*

cp -r ../master/target/scala-2.9.1/classes.sxr/* ./main/
cp -r ../master/target/scala-2.9.1/test-classes.sxr/* ./test/

git add \*.html

git commit -a

