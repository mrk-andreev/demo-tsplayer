FROM python:3.8-alpine
ENV PYTHONUNBUFFERED=1 \
    NOTVISIBLE="in users profile" \
    WORK_DIR=/opt/app \
    PYTHONPATH=/opt/app
WORKDIR ${WORK_DIR}

COPY ./requirements.txt /tmp/requirements.txt

RUN pip install  --default-timeout=1000 --prefer-binary --no-cache-dir -r /tmp/requirements.txt

COPY . /opt/app

CMD ["python", "src/upload_history_snippet.py"]