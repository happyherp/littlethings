import db, models

models.Base.metadata.create_all(db.engine) 