docs-rap: docs/raport.tex
	pdflatex -halt-on-error -output-directory docs docs/raport.tex
	@rm docs/*.aux
	@rm docs/*.log
	@echo "[docs-rap] done docs/raport.pdf, logs cleaned up"

docs: docs-rap
	@echo "done docs"