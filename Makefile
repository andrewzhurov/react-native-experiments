install: 
	npm install

## TODO pass args from `make run ...` to `babel-node ...`
run:
	npm run babel-node -- src/bin/gendiff.js

publish:
	npm publish

lint:
	npm run eslint -- src test

test:
	npm test

check: lint test

build:
	rm -rf dist
	npm run build

auto_publish: install build publish

git_all:
	git add ./
	git commit -m "$(MSG)"
	git push origin master

pp:
	npm version patch --no-git-tag-version
	npm publish
mp:
	npm version minor --no-git-tag-version
	npm publish
pp!:
	npm version patch --force
	npm publish

## Seems it's not supposed to be used this way
patch: 
	$(eval MSG ?= "Patch")
	git add ./
	npm version patch --force --message "$(MSG) %s"
	git push origin master
	npm publish
minor: 
	$(eval MSG ?= "Minor")
	git add ./
	npm version minor --force --message "$(MSG) %s"
	git push origin master
	npm publish
