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
