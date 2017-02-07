SELECT id, htek_id, start_time, stop_time, record, status, coil_part_number, coil_width, coil_meas_weight, coil_cal_weight, coil_thickness 
FROM dfir.regf order by id desc limit 100;

SELECT pssschtelid, coilid, steelgrade, width, thickness, weight, corediameter, ts_kuldes, exitcoilsno, processtype, elongation, elonglowlim, elonguplim 
FROM dfir.szurasterv_log order by pssschtelid desc limit 100;

SELECT * FROM dfir.szurasterv_log  LEFT JOIN (Select * From regf_log As a order by id desc limit 100)As a ON (szurasterv_log.coilid = a.htek_id );

SELECT * FROM dfir.szurasterv_log  LEFT JOIN (Select * From regf_log As a order by id desc limit 100)As a ON (szurasterv_log.coilid = a.htek_id ) where id > 0 order by szurasterv_log.pssschtelid desc;

