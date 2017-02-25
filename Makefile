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
	npm version patch
	npm publish
mp:
	npm version minor
	npm publish
pp!:
	npm version patch --force
	npm publish

gpp: 
	$(eval MSG ?= "Patch")
	make git_all MSG="$(MSG)"
	make pp
gmp: 
	$(eval MSG ?= "Minor")
	make git_all MSG="$(MSG)"
	make mp
