import logging


def get_logger(name, level):
    logger = logging.getLogger(name)
    logger.setLevel(level)
    logger_handler = logging.StreamHandler()
    logger_handler.setFormatter(logging.Formatter('%(asctime)s %(process)d %(processName)s %(name)-12s:%(lineno)d '
                                                  '%(levelname)-8s %(message)s'))
    logger.addHandler(logger_handler)

    return logger
