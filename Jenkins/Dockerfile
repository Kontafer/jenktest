FROM python:3.7-rc

LABEL version="1.0"
EXPOSE 5050
ADD greetings_app/ /greetings_app/
RUN pip install -r /greetings_app/requirements.txt

ENV DB_URL=sqlite:///foo.db

CMD ["python", "greetings_app/app.py"]

