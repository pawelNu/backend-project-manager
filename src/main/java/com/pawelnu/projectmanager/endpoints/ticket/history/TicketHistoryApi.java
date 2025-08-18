package com.pawelnu.projectmanager.endpoints.ticket.history;

import com.pawelnu.projectmanager.endpoints.ticket.history.dto.TicketHistoryCreateRequestDTO;
import com.pawelnu.projectmanager.endpoints.ticket.history.dto.TicketHistoryDTO;
import com.pawelnu.projectmanager.endpoints.ticket.history.dto.TicketHistoryEditRequestDTO;
import com.pawelnu.projectmanager.exception.model.SimpleResponse;
import com.pawelnu.projectmanager.utils.Path;
import com.pawelnu.projectmanager.utils.ResponseErrors;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Ticket Histories")
@RequestMapping(Path.API_TICKET_HISTORIES)
public interface TicketHistoryApi {

  @ApiResponse(
      responseCode = "201",
      description = "Created",
      content = {
        @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = TicketHistoryDTO.class))
      })
  @ResponseErrors
  @PostMapping("")
  @Operation(description = "Add new ticket history.")
  ResponseEntity<TicketHistoryDTO> create(@Valid @RequestBody TicketHistoryCreateRequestDTO body);

  @Operation(
      description =
          "List ticket histories with filtering, sorting and pagination (react-admin format)")
  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              array = @ArraySchema(schema = @Schema(implementation = TicketHistoryDTO.class))))
  @ResponseErrors
  @GetMapping("")
  ResponseEntity<List<TicketHistoryDTO>> getList(
      @Parameter(description = "Sort as JSON string, e.g. [\"title\",\"ASC\"]")
          @RequestParam(required = false)
          String sort,
      @Parameter(description = "Range as JSON string, e.g. [0, 24]") @RequestParam(required = false)
          String range,
      @Parameter(description = "Filter as JSON string, e.g. {\"title\":\"bar\"}")
          @RequestParam(required = false)
          String filter);

  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content = {
        @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = TicketHistoryDTO.class))
      })
  @ResponseErrors
  @GetMapping("/{id}")
  @Operation(description = "Get ticket history by id.")
  ResponseEntity<TicketHistoryDTO> getById(@Parameter() @PathVariable() UUID id);

  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content = {
        @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = TicketHistoryDTO.class))
      })
  @ResponseErrors
  @PutMapping("/{id}")
  @Operation(description = "Edit ticket history by id.")
  ResponseEntity<TicketHistoryDTO> editById(
      @Parameter() @PathVariable() UUID id, @Valid @RequestBody TicketHistoryEditRequestDTO body);

  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content = {
        @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = SimpleResponse.class))
      })
  @ResponseErrors
  @DeleteMapping("/{id}")
  @Operation(description = "Delete ticket history by id.")
  ResponseEntity<SimpleResponse> deleteById(@Parameter() @PathVariable() UUID id);
}
