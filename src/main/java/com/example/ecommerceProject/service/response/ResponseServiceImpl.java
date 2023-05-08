package com.example.ecommerceProject.service.response;


import com.example.ecommerceProject.dto.ResponseDto;
import com.example.ecommerceProject.repository.ResponseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ResponseServiceImpl implements ResponseService {
    @Override
    public ResponseEntity<ResponseDto> success(Object body, String message) {
        if (message == null) {
            return ResponseEntity.ok().body(ResponseDto.builder()
                    .body(body)
                    .code(HttpStatus.OK)
                    .message("Request process successfully")
                    .build());
        } else {
            return ResponseEntity.ok().body(ResponseDto.builder()
                    .body(body)
                    .code(HttpStatus.OK)
                    .message(message)
                    .build());
        }
    }

    @Override
    public ResponseEntity<ResponseDto> created(Object body, String message) {
        if (message == null) {
            return ResponseEntity.ok().body(ResponseDto.builder()
                    .body(body)
                    .code(HttpStatus.CREATED)
                    .message("Created successfully")
                    .build());
        } else {
            return ResponseEntity.ok().body(ResponseDto.builder()
                    .body(body)
                    .code(HttpStatus.CREATED)
                    .message(message)
                    .build());
        }
    }

    //    public ResponseEntity<ResponseDto> notFound(Object body,String message){
//        if (message==null){
//            return ResponseEntity.ok().body(ResponseDto.builder()
//                            .body(body)
//                            .code(HttpStatus.NOT_FOUND)
//                            .message("Not found")
//                    .build());
//        } else {
//            return ResponseEntity.ok().body(ResponseDto.builder()
//                    .body(body)
//                    .code(HttpStatus.NOT_FOUND)
//                            .message(message)
//                    .build());
//        }
//    }
    @Override
    public ResponseEntity<ResponseDto> badRequest(Object body, String message) {
        if (message == null) {
            return ResponseEntity.badRequest().body(ResponseDto.builder()
                    .body(body)
                    .code(HttpStatus.BAD_REQUEST)
                    .message("Bad Request")
                    .build());
        } else {
            return ResponseEntity.badRequest().body(ResponseDto.builder()
                    .body(body)
                    .code(HttpStatus.BAD_REQUEST)
                    .message(message)
                    .build());
        }
    }

    @Override
    public ResponseEntity<ResponseDto> locked(Object body, String message) {
        if (message == null) {
            return ResponseEntity.badRequest().body(ResponseDto.builder()
                    .body(body)
                    .code(HttpStatus.LOCKED)
                    .message("Account Locked")
                    .build());
        } else {
            return ResponseEntity.badRequest().body(ResponseDto.builder()
                    .body(body)
                    .code(HttpStatus.LOCKED)
                    .message(message)
                    .build());
        }
    }
}
